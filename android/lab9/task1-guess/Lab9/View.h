#ifndef GAMEVIEWMODEL_H
#define GAMEVIEWMODEL_H

#include <QObject>
#include <QSettings>
#include <QUuid>
#include "Game.h"

class GameViewModel : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString message READ message NOTIFY messageChanged)
    Q_PROPERTY(int launchCount READ launchCount NOTIFY launchCountChanged)
    Q_PROPERTY(QString uniqueId READ uniqueId NOTIFY uniqueIdChanged)
    Q_PROPERTY(int attempts READ attempts NOTIFY attemptsChanged)
    Q_PROPERTY(int totalScore READ totalScore NOTIFY totalScoreChanged)
    Q_PROPERTY(int bestScore READ bestScore NOTIFY bestScoreChanged)

public:
    explicit GameViewModel(QObject *parent = nullptr);

    Q_INVOKABLE bool checkGuess(const QString &guess);
    Q_INVOKABLE void resetGame();
    Q_INVOKABLE void restartApp();
    QString message() const;
    int launchCount() const;
    QString uniqueId() const;
    int attempts() const;
    int totalScore() const;
    int bestScore() const;

signals:
    void messageChanged();
    void launchCountChanged();
    void uniqueIdChanged();
    void attemptsChanged();
    void totalScoreChanged();
    void bestScoreChanged();
    void attemptsDialogRequested(int attempts);

private slots:
    void onGameWon();
    void onGuessFeedback(const QString &feedback);

private:
    void loadSettings();
    void saveSettings();

    GameModel m_gameModel;
    QString m_message;
    int m_launchCount;
    QString m_uniqueId;
    int m_attempts;
    int m_totalScore;
    int m_bestScore;
    QSettings m_settings;
};

#endif // GAMEVIEWMODEL_H
