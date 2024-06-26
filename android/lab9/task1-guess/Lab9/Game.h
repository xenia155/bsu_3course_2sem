#ifndef GAMEMODEL_H
#define GAMEMODEL_H

#include <QObject>
#include <QRandomGenerator>

class GameModel : public QObject
{
    Q_OBJECT
    Q_PROPERTY(int getNumber READ getNumber NOTIFY numberChanged)

public:
    explicit GameModel(QObject *parent = nullptr);
    void setSecretNumber(int getNumber);
    int getNumber() const;

public slots:
    void generateNumber();
    bool checkGuess(int guess);

signals:
    void numberChanged();
    void gameWon();
    void gameLost();
    void guessResult(bool correctGuess, const QString &feedback);
    void guessFeedback(const QString &feedback);

private:
    int m_number;
    int m_secretNumber;
};

#endif // GAMEMODEL_H
