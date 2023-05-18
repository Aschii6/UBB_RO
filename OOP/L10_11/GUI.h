#pragma once

#include <QtWidgets>
#include <QCommonStyle>
#include "Service.h"


class BibliotecaGUI : public QWidget {
private:
    Service& serv;

    QHBoxLayout* lyMain = new QHBoxLayout{};
    QVBoxLayout* lyLeft = new QVBoxLayout{};
    QVBoxLayout* lyRight = new QVBoxLayout{};

    QVBoxLayout* lyFarRight = new QVBoxLayout{};
    
    QHBoxLayout* lyUp = new QHBoxLayout{};
    QGridLayout* btnGroup = new QGridLayout{};

    QListWidget* lst = new QListWidget{};

    QTableWidget* table = new QTableWidget{};

    QPushButton* btnAdd = new QPushButton{ "&Adauga" };
    QPushButton* btnDelete = new QPushButton{ "&Sterge" };
    QPushButton* btnModify = new QPushButton{ "&Modifica" };
    QPushButton* btnSearch = new QPushButton{ "&Cauta" };
    QPushButton* btnFilterByTitle = new QPushButton{ "&Filtrare dupa titlu" };
    QPushButton* btnFilterByYear = new QPushButton{ "&Filtrare dupa an aparitie" };
    QPushButton* btnSortByTitle = new QPushButton{ "&Sortare dupa titlu" };
    QPushButton* btnSortByAuthor = new QPushButton{ "&Sortare dupa autor" };
    QPushButton* btnSortByYearAndGenre = new QPushButton{ "&Sortare dupa an aparitie si gen" };
    QPushButton* btnAddToCos = new QPushButton{ "&Adauga in cos" };
    QPushButton* btnEmptyCos = new QPushButton{ "&Goleste cos" };
    QPushButton* btnFillCos = new QPushButton{ "&Umple cos" };
    QPushButton* btnExportCos = new QPushButton{ "&Export cos csv" };
    QPushButton* btnUndo = new QPushButton{ "&Undo" };

    QPushButton* btnShowCos = new QPushButton{ "&Arata Cos" };

    QLineEdit* textTitle = new QLineEdit{};
    QLineEdit* textAuthor = new QLineEdit{};
    QLineEdit* textGenre = new QLineEdit{};
    QLineEdit* textYear = new QLineEdit{};

    void addComponents();
    void showLst(vector<Carte> carti);
    void connectSignals();
    void refreshButtonsFarRight();

    void highlightFiltered(vector<Carte> cF);

    QWidget* wFill = new QWidget;
    QLineEdit* textFill = new QLineEdit;
    QPushButton* btnFill = new QPushButton("Enter");
    QVBoxLayout* lyFill = new QVBoxLayout;

    QWidget* wExp = new QWidget;
    QLineEdit* textExp = new QLineEdit;
    QPushButton* btnExp = new QPushButton("Enter");
    QVBoxLayout* lyExp = new QVBoxLayout;

public:
    BibliotecaGUI(Service& serv) : serv{ serv } {
        addComponents();
        showLst(serv.getAllService());
        connectSignals();
        //refreshButtonsFarRight();
    }
};

class CosGUI : public QWidget {
private:
    vector<Carte> carti;
    QListWidget* lstCos = new QListWidget{};
    void showCos() {
        for (auto& c : carti) {
            QListWidgetItem* item = new QListWidgetItem{ QString::fromStdString(c.getTitlu() + " - " + c.getAutor() + " - " + c.getGen() + " - " + std::to_string(c.getAnAparitie())) };
            lstCos->addItem(item);
        }
    }
public:
    CosGUI(vector<Carte> carti) : carti{ carti } {
		setLayout(new QHBoxLayout{});
		layout()->addWidget(lstCos);
        showCos();
	}
};
