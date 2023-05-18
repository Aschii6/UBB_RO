#include "GUI.h"
#include <qdebug.h>
#include <qabstractitemview.h>

#include <map>
using std::map;


void BibliotecaGUI::addComponents() {
	setLayout(lyMain);
	lyMain->addLayout(lyLeft);
	lyMain->addLayout(lyRight);

	lyMain->addLayout(lyFarRight);

	lyLeft->addLayout(lyUp);
	lyLeft->addLayout(btnGroup);
	//lyRight->addWidget(lst);
	lyRight->addWidget(table);
	table->setSelectionBehavior(QAbstractItemView::SelectRows);

	lyFarRight->addWidget(lst);
	
	// Buttons
	btnGroup->addWidget(btnAdd,0,0);
	btnGroup->addWidget(btnDelete,0,1);
	btnGroup->addWidget(btnModify);
	btnGroup->addWidget(btnSearch);
	btnGroup->addWidget(btnFilterByTitle);
	btnGroup->addWidget(btnFilterByYear);
	btnGroup->addWidget(btnSortByTitle);
	btnGroup->addWidget(btnSortByAuthor);
	btnGroup->addWidget(btnSortByYearAndGenre);
	btnGroup->addWidget(btnAddToCos);
	btnGroup->addWidget(btnEmptyCos);
	btnGroup->addWidget(btnFillCos);
	btnGroup->addWidget(btnExportCos);
	btnGroup->addWidget(btnUndo);
	btnGroup->addWidget(btnShowCos);

	// Input fields
	QFormLayout* form = new QFormLayout{};
	form->addRow("Titlu", textTitle);
	form->addRow("Autor", textAuthor);
	form->addRow("Gen", textGenre);
	form->addRow("An", textYear);
	lyUp->addLayout(form);

	lyFill->addWidget(textFill);
	lyFill->addWidget(btnFill);
	wFill->setLayout(lyFill);

	lyExp->addWidget(textExp);
	lyExp->addWidget(btnExp);
	wExp->setLayout(lyExp);
}

void BibliotecaGUI::showLst(vector<Carte> carti) {
	lst->clear();
	for (const auto& c : carti) {
		QListWidgetItem* item = new QListWidgetItem{ QString::fromStdString(c.getTitlu() + " - " + c.getAutor() + " - " + c.getGen() + " - " + std::to_string(c.getAnAparitie())) };
		item->setToolTip("Titlu: " + QString::fromStdString(c.getTitlu()) + "\nAutor: " + QString::fromStdString(c.getAutor()) + "\nGen: " + QString::fromStdString(c.getGen()) + "\nAn aparitie: " + QString::fromStdString(std::to_string(c.getAnAparitie())));
		lst->addItem(item);
	}

	table->clear();
	table->setRowCount(carti.size());
	table->setColumnCount(4);
	int row = 0;
	for (const auto& c : carti) {
		table->setItem(row, 0, new QTableWidgetItem{ QString::fromStdString(c.getTitlu()) });
		table->setItem(row, 1, new QTableWidgetItem{ QString::fromStdString(c.getAutor()) });
		table->setItem(row, 2, new QTableWidgetItem{ QString::fromStdString(c.getGen()) });
		table->setItem(row, 3, new QTableWidgetItem{ QString::fromStdString(std::to_string(c.getAnAparitie())) });
		row++;
	}
}

void BibliotecaGUI::connectSignals() {
	QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
		auto titlu = textTitle->text();
		auto autor = textAuthor->text();
		auto gen = textGenre->text();
		auto an = textYear->text();

		textTitle->clear();
		textAuthor->clear();
		textGenre->clear();
		textYear->clear();

		try
		{
			serv.addService(titlu.toStdString(), autor.toStdString(), gen.toStdString(), an.toInt());
			showLst(serv.getAllService());
		}
		catch (RepoException re)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.getMsg()));
		}
		catch (ValidatorException ve)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(ve.getMsg()));
		}
		//refreshButtonsFarRight();
		});


	QObject::connect(btnDelete, &QPushButton::clicked, [&]() {
		//auto sel = lst->selectedItems();
		//if (sel.isEmpty())
		//{
		//	QMessageBox::warning(this, "Warning", "Nicio carte selectata");
		//	return;
		//}
		//try
		//{
		//	QString text = sel.at(0)->text();
		//	QStringList parts = text.split(" - ");
		//	serv.deleteService(parts[0].toStdString(), parts[1].toStdString());
		//	showLst(serv.getAllService());
		//	//refreshButtonsFarRight();
		//}
		//catch (const std::exception&)
		//{
		//}

		auto sel = table->selectedItems();
		if (sel.isEmpty())
		{
			QMessageBox::warning(this, "Warning", "Nicio carte selectata");
			return;
		}
		try
		{
			string titlu = sel.at(0)->text().toStdString();
			string autor = sel.at(1)->text().toStdString();
			serv.deleteService(titlu, autor);
			showLst(serv.getAllService());
			//refreshButtonsFarRight();
		}
		catch (const std::exception&)
		{}
		});


	QObject::connect(btnModify, &QPushButton::clicked, [&]() {
		auto titlu = textTitle->text();
		auto autor = textAuthor->text();
		auto gen = textGenre->text();
		auto an = textYear->text();

		textTitle->clear();
		textAuthor->clear();
		textGenre->clear();
		textYear->clear();

		try
		{
			serv.modifyService(titlu.toStdString(), autor.toStdString(), gen.toStdString(), an.toInt());
			showLst(serv.getAllService());
		}
		catch (RepoException re)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.getMsg()));
		}
		catch (ValidatorException ve)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(ve.getMsg()));
		}
		//refreshButtonsFarRight();
		});


	QObject::connect(btnSearch, &QPushButton::clicked, [&]() {
		auto titlu = textTitle->text();
		auto autor = textAuthor->text();

		textTitle->clear();
		textAuthor->clear();
		textGenre->clear();
		textYear->clear();

		try
		{
			auto carte = serv.searchService(titlu.toStdString(), autor.toStdString());
			QMessageBox::about(this, "Info", QString::fromStdString(carte.getTitlu() + " - " + carte.getAutor() + " - " + carte.getGen() + " - " + std::to_string(carte.getAnAparitie())));
		}
		catch (RepoException re)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.getMsg()));
		}
		catch (ValidatorException ve)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(ve.getMsg()));
		}
		});


	QObject::connect(btnSortByTitle, &QPushButton::clicked, [&]() {
		showLst(serv.sortByTitlu());
		});


	QObject::connect(btnSortByAuthor, &QPushButton::clicked, [&]() {
		showLst(serv.sortByAutor());
		});


	QObject::connect(btnSortByYearAndGenre, &QPushButton::clicked, [&]() {
		showLst(serv.sortByAnSiGen());
		});


	QObject::connect(btnFilterByTitle, &QPushButton::clicked, [&]() {
		if (textTitle->text().isEmpty())
		{
			showLst(serv.getAllService());
			return;
		}
		auto title = textTitle->text().toStdString();
		textTitle->clear();
		vector<Carte> cartiFiltrate = serv.filterByTitlu(title);

		highlightFiltered(cartiFiltrate);
		});

	
	QObject::connect(btnFilterByYear, &QPushButton::clicked, [&]() {
		if (textYear->text().isEmpty())
		{
			showLst(serv.getAllService());
			return;
		}
		auto year = textYear->text().toInt();
		textYear->clear();

		vector<Carte> cartiFiltrate = serv.filterByAnAparitie(year);
		highlightFiltered(cartiFiltrate);
		});


	QObject::connect(btnUndo, &QPushButton::clicked, [&]() {
		try
		{
			serv.undo();
			showLst(serv.getAllService());
		}
		catch (UndoException ue)
		{
			QMessageBox::warning(this, "Warning", QString::fromStdString(ue.getMsg()));
		}
		});


	QObject::connect(btnAddToCos, &QPushButton::clicked, [&]() {
		auto sel = lst->selectedItems();
		if (sel.isEmpty())
		{
			QMessageBox::warning(this, "Warning", "Nicio carte selectata");
			return;
		}
		try
		{
			QString text = sel.at(0)->text();
			QStringList parts = text.split(" - ");
			serv.addCos(serv.searchService(parts[0].toStdString(), parts[1].toStdString()));
		}
		catch (const std::exception&)
		{

		}
		});


	QObject::connect(btnEmptyCos, &QPushButton::clicked, [&]() {
		serv.emptyCos();
		});


	QObject::connect(btnFillCos, &QPushButton::clicked, [&]() {
		wFill->show();

		QObject::connect(btnFill, &QPushButton::clicked, [&]() {
			
			int nr = textFill->text().toInt();
			textFill->clear();
			serv.fillCos(nr);
			wFill->close();
			});
		});


	QObject::connect(btnExportCos, &QPushButton::clicked, [&]() {
		wExp->show();

		QObject::connect(btnExp, &QPushButton::clicked, [&]() {
			auto path = textExp->text().toStdString();
			textExp->clear();
			serv.exportCSV(path);
			wExp->close();
			});
		});


	QObject::connect(btnShowCos, &QPushButton::clicked, [&]() {
		CosGUI* cos = new CosGUI{serv.getAllCos()};
		cos->show();
		});
}

void BibliotecaGUI::refreshButtonsFarRight(){

	while (QLayoutItem* item = lyFarRight->takeAt(0)) {
		if (QWidget* widget = item->widget()) {
			// If the item is a widget, remove it from the layout and delete it
			lyFarRight->removeWidget(widget);
			delete widget;
		}
		else {
			// If the item is a layout, delete it recursively
			delete item->layout();
		}
		// Delete the item itself
		delete item;
	}

	map <string,vector<Carte>> dict = serv.raportGen();
	for (const auto& item : dict) {
		string key = item.first;
		int nr = item.second.size();

		auto btn = new QPushButton(QString::fromStdString(key));
		lyFarRight->addWidget(btn);
		QObject::connect(btn, &QPushButton::clicked, [this, nr]() {
			QMessageBox::about(this, "Info", QString::number(nr));
		});
	}
}

void BibliotecaGUI::highlightFiltered(vector<Carte> cF) {
	for (int i = 0; i < lst->count(); i++)
	{
		auto item = lst->item(i);
		QString text = item->text();
		QStringList parts = text.split(" - ");
		Carte c{ parts[0].toStdString(), parts[1].toStdString(), parts[2].toStdString(), parts[3].toInt() };
		if (std::find(cF.begin(), cF.end(), c) != cF.end())
		{
			item->setBackgroundColor(Qt::green);
		}
		else
		{
			item->setBackgroundColor(Qt::white);
		}
	}

	for (int i = 0; i < table->rowCount(); ++i) {
		auto titlu = table->item(i, 0)->text().toStdString();
		auto autor = table->item(i, 1)->text().toStdString();

		Carte c{ titlu, autor, "", 0 };
		if (std::find(cF.begin(), cF.end(), c) != cF.end())
		{
			table->item(i, 0)->setBackgroundColor(Qt::green);
			table->item(i, 1)->setBackgroundColor(Qt::green);
			table->item(i, 2)->setBackgroundColor(Qt::green);
			table->item(i, 3)->setBackgroundColor(Qt::green);
		}
		else
		{
			table->item(i, 0)->setBackgroundColor(Qt::white);
			table->item(i, 1)->setBackgroundColor(Qt::white);
			table->item(i, 2)->setBackgroundColor(Qt::white);
			table->item(i, 3)->setBackgroundColor(Qt::white);
		}
	}
}
