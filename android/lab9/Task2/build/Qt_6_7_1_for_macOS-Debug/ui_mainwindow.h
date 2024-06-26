/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QTableView>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QPushButton *buttonShowData;
    QPushButton *buttonAddData;
    QPushButton *buttonUpdateData;
    QTableView *tableView;
    QPushButton *buttonGroupBy;
    QPushButton *buttonSumScores;
    QPushButton *buttonSortByScore;
    QMenuBar *menubar;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName("MainWindow");
        MainWindow->resize(800, 600);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName("centralwidget");
        buttonShowData = new QPushButton(centralwidget);
        buttonShowData->setObjectName("buttonShowData");
        buttonShowData->setGeometry(QRect(20, 20, 93, 28));
        buttonAddData = new QPushButton(centralwidget);
        buttonAddData->setObjectName("buttonAddData");
        buttonAddData->setGeometry(QRect(130, 20, 93, 28));
        buttonUpdateData = new QPushButton(centralwidget);
        buttonUpdateData->setObjectName("buttonUpdateData");
        buttonUpdateData->setGeometry(QRect(240, 20, 93, 28));
        tableView = new QTableView(centralwidget);
        tableView->setObjectName("tableView");
        tableView->setGeometry(QRect(20, 70, 751, 500));
        buttonGroupBy = new QPushButton(centralwidget);
        buttonGroupBy->setObjectName("buttonGroupBy");
        buttonGroupBy->setGeometry(QRect(370, 20, 75, 24));
        buttonSumScores = new QPushButton(centralwidget);
        buttonSumScores->setObjectName("buttonSumScores");
        buttonSumScores->setGeometry(QRect(480, 20, 75, 24));
        buttonSortByScore = new QPushButton(centralwidget);
        buttonSortByScore->setObjectName("buttonSortByScore");
        buttonSortByScore->setGeometry(QRect(600, 20, 75, 24));
        MainWindow->setCentralWidget(centralwidget);
        menubar = new QMenuBar(MainWindow);
        menubar->setObjectName("menubar");
        menubar->setGeometry(QRect(0, 0, 800, 22));
        MainWindow->setMenuBar(menubar);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName("statusbar");
        MainWindow->setStatusBar(statusbar);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));
        buttonShowData->setText(QCoreApplication::translate("MainWindow", "Show Data", nullptr));
        buttonAddData->setText(QCoreApplication::translate("MainWindow", "Add Record", nullptr));
        buttonUpdateData->setText(QCoreApplication::translate("MainWindow", "Update Record", nullptr));
        buttonGroupBy->setText(QCoreApplication::translate("MainWindow", "Group", nullptr));
        buttonSumScores->setText(QCoreApplication::translate("MainWindow", "Sum", nullptr));
        buttonSortByScore->setText(QCoreApplication::translate("MainWindow", "Sort", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
