#include "Game.h"

GameModel::GameModel(QObject *parent) : QObject(parent), m_number(0)
{
    generateNumber();
}

int GameModel::getNumber() const
{
    return m_number;
}

void GameModel::setSecretNumber(int number)
{
    m_secretNumber = number;
}

void GameModel::generateNumber()
{
    m_number = QRandomGenerator::global()->bounded(1, 101);
    emit numberChanged();
}

bool GameModel::checkGuess(int guess)
{
    if (guess == m_number) {
        emit gameWon();
        return true;
    } else {
        if (guess < m_number) {
            emit guessFeedback("Too low");
        } else {
            emit guessFeedback("Too high");
        }
        return false;
    }
}
