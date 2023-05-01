//
// Created by Daniel on 30-Apr-23.
//
#include "Carte.h"
#include "Repo.h"

#pragma once

class ActiuneUndo {
public:
    virtual void doUndo() = 0;
    virtual ~ActiuneUndo() = default;
};

class UndoAdd: public ActiuneUndo {
private:
    Carte carteAdaugata;
    Repo &repo;
public:
    UndoAdd(Repo &repo, const Carte &carte): carteAdaugata{carte}, repo{repo} {}
    void doUndo() override{
        repo.deleteCarte(carteAdaugata);
    }
};

class UndoDelete: public ActiuneUndo {
private:
    Carte carteStearsa;
    Repo &repo;
public:
    UndoDelete(Repo &repo, const Carte &carte): carteStearsa{carte}, repo{repo} {}
    void doUndo() override{
        repo.addCarte(carteStearsa);
    }
};

class UndoModify: public ActiuneUndo {
private:
    Carte carteInainteDeModificare;
    Repo &repo;
public:
    UndoModify(Repo &repo, const Carte &carte): carteInainteDeModificare{carte}, repo{repo} {}
    void doUndo() override{
        repo.modifyCarte(carteInainteDeModificare);
    }
};
