#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QSqlQuery>
#include <QSqlError>
#include <QMessageBox>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    initializeDatabase();

    createTables();
    populateInitialData();

    model = new QSqlTableModel(this, db);
    model->setTable("Car");
    model->select();
    ui->tableView->setModel(model);

    queryModel = new QSqlQueryModel(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::initializeDatabase()
{
    db = QSqlDatabase::addDatabase("QSQLITE");
    db.setDatabaseName("cars.db");

    if (!db.open()) {
        QMessageBox::critical(this, "Error", "Failed to open database: " + db.lastError().text());
    }
}

void MainWindow::createTables()
{
    QSqlQuery query;
    query.exec("CREATE TABLE IF NOT EXISTS Car ("
               "id INTEGER PRIMARY KEY AUTOINCREMENT, "
               "brand TEXT, "
               "serial_number TEXT, "
               "year INTEGER)");

    if (query.lastError().isValid()) {
        QMessageBox::critical(this, "Error", "Failed to create table: " + query.lastError().text());
    }
}

void MainWindow::populateInitialData()
{
    QSqlQuery query;
    query.exec("DELETE FROM Car");

    query.exec("INSERT INTO Car (brand, serial_number, year) VALUES "
               "('Toyota', '123456', 2010), "
               "('Ford', '654321', 2012), "
               "('BMW', '112233', 2015), "
               "('Audi', '445566', 2018), "
               "('Mercedes', '778899', 2020)");

    if (query.lastError().isValid()) {
        QMessageBox::critical(this, "Error", "Failed to populate initial data: " + query.lastError().text());
    }
}

void MainWindow::on_buttonShowData_clicked()
{
    model->select();
    ui->tableView->setModel(model);
}

void MainWindow::on_buttonAddData_clicked()
{
    QSqlQuery query;
    query.exec("INSERT INTO Car (brand, serial_number, year) VALUES "
               "('Honda', '998877', 2021)");

    if (query.lastError().isValid()) {
        QMessageBox::critical(this, "Error", "Failed to add data: " + query.lastError().text());
    } else {
        model->select();  // обновить данные в таблице
    }
}

void MainWindow::on_buttonUpdateData_clicked()
{
    QModelIndexList selected = ui->tableView->selectionModel()->selectedRows();
    if (selected.isEmpty()) {
        QMessageBox::warning(this, "Warning", "No row selected for update");
        return;
    }

    int row = selected.first().row();
    int id = model->data(model->index(row, 0)).toInt();

    QSqlQuery query;
    query.prepare("UPDATE Car SET brand = ?, serial_number = ?, year = ? WHERE id = ?");
    query.addBindValue("UpdatedBrand");
    query.addBindValue("UpdatedSerialNumber");
    query.addBindValue(2022);
    query.addBindValue(id);

    if (query.exec()) {
        model->select();
    } else {
        QMessageBox::critical(this, "Error", "Failed to update data: " + query.lastError().text());
    }
}

void MainWindow::on_buttonGroupBy_clicked()
{
    queryModel->setQuery("SELECT brand, COUNT(*) as count FROM Car GROUP BY brand");
    if (queryModel->lastError().isValid()) {
        QMessageBox::critical(this, "Error", "Failed to group data: " + queryModel->lastError().text());
    } else {
        ui->tableView->setModel(queryModel);
    }
}

void MainWindow::on_buttonSumScores_clicked()
{
    queryModel->setQuery("SELECT SUM(year) as total_year FROM Car");
    if (queryModel->lastError().isValid()) {
        QMessageBox::critical(this, "Error", "Failed to sum years: " + queryModel->lastError().text());
    } else {
        ui->tableView->setModel(queryModel);
    }
}

void MainWindow::on_buttonSortByScore_clicked()
{
    model->setTable("Car");
    model->setSort(3, Qt::AscendingOrder); // 3 - индекс столбца year
    model->select();
    ui->tableView->setModel(model);
}
