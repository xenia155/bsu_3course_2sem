#include "View.h"

GameViewModel::GameViewModel(QObject *parent) : QObject(parent), m_settings("YourCompany", "GuessNumberApp")
{
    connect(&m_gameModel, &GameModel::gameWon, this, &GameViewModel::onGameWon);
    connect(&m_gameModel, &GameModel::guessFeedback, this, &GameViewModel::onGuessFeedback);

    loadSettings();

    m_launchCount++;
    saveSettings();
    emit launchCountChanged();
}

bool GameViewModel::checkGuess(const QString &guess)
{
    bool ok;
    int number = guess.toInt(&ok);
    if (ok) {
        m_attempts++;
        emit attemptsChanged();
        if (m_gameModel.checkGuess(number)) {
            emit attemptsDialogRequested(m_attempts);
            return true;
        } else {
            return false;
        }
    } else {
        m_message = "Invalid input!";
        emit messageChanged();
        return false;
    }
}

QString GameViewModel::message() const
{
    return m_message;
}

int GameViewModel::launchCount() const
{
    return m_launchCount;
}

QString GameViewModel::uniqueId() const
{
    return m_uniqueId;
}

int GameViewModel::attempts() const
{
    return m_attempts;
}

int GameViewModel::totalScore() const
{
    return m_totalScore;
}

int GameViewModel::bestScore() const
{
    return m_bestScore;
}

void GameViewModel::resetGame()
{
    m_gameModel.generateNumber();
    m_message = "";
    m_attempts = 0;
    emit messageChanged();
    emit attemptsChanged();
}

void GameViewModel::restartApp()
{

}

void GameViewModel::onGameWon()
{
    m_message = "You guessed it!";
    m_totalScore += 10;
    if (m_totalScore > m_bestScore) {
        m_bestScore = m_totalScore;
        saveSettings();
    }
    emit messageChanged();
    emit totalScoreChanged();
    emit bestScoreChanged();
}

void GameViewModel::onGuessFeedback(const QString &feedback)
{
    m_message = feedback;
    emit messageChanged();
}

void GameViewModel::loadSettings()
{
    m_launchCount = m_settings.value("launchCount", 0).toInt();
    m_uniqueId = m_settings.value("uniqueId", QUuid::createUuid().toString()).toString();
    m_attempts = m_settings.value("attempts", 0).toInt();
    m_totalScore = m_settings.value("totalScore", 0).toInt();
    m_bestScore = m_settings.value("bestScore", 0).toInt();
}

void GameViewModel::saveSettings()
{
    m_settings.setValue("launchCount", m_launchCount);
    m_settings.setValue("uniqueId", m_uniqueId);
    m_settings.setValue("attempts", m_attempts);
    m_settings.setValue("totalScore", m_totalScore);
    m_settings.setValue("bestScore", m_bestScore);
}
