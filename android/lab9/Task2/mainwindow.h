#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QSqlDatabase>
#include <QSqlTableModel>
#include <QSqlQueryModel>

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void on_buttonShowData_clicked();
    void on_buttonAddData_clicked();
    void on_buttonUpdateData_clicked();
    void on_buttonGroupBy_clicked();
    void on_buttonSumScores_clicked();
    void on_buttonSortByScore_clicked();

private:
    Ui::MainWindow *ui;
    QSqlDatabase db;
    QSqlTableModel *model;
    QSqlQueryModel *queryModel;

    void initializeDatabase();
    void createTables();
    void populateInitialData();
};

#endif // MAINWINDOW_H
