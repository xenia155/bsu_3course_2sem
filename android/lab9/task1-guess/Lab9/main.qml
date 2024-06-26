import QtQuick 2.12
import QtQuick.Controls 2.5
import by.xenia.guessnumber 1.0

ApplicationWindow {
    visible: true
    width: 640
    height: 480
    title: qsTr("Guess the Number")

    GameViewModel {
        id: viewModel
        onAttemptsDialogRequested: {
            attemptsDialogLabel.text = qsTr("Number guessed in %1 attempts.").arg(attempts)
            attemptsDialog.open()
        }
    }

    Rectangle {
        anchors.fill: parent
        color: "#f0f0f0"

        Column {
            width: parent.width
            spacing: 20
            padding: 20

            TextField {
                id: guessInput
                placeholderText: qsTr("Enter number")
                font.pixelSize: 18
                anchors.horizontalCenter: parent.horizontalCenter
            }

            Button {
                id: submitButton
                text: qsTr("Enter value")
                font.pixelSize: 18
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: {
                    viewModel.checkGuess(guessInput.text)
                    scaleAnimation.start()
                }
            }

            Label {
                id: messageLabel
                text: viewModel.message
                color: "red"
                font.pixelSize: 18
                anchors.horizontalCenter: parent.horizontalCenter
            }

            Button {
                text: qsTr("Reset")
                font.pixelSize: 18
                anchors.horizontalCenter: parent.horizontalCenter
                onClicked: viewModel.resetGame()
            }

            Rectangle {
                width: parent.width
                height: 1
                color: "#cccccc"
                anchors.horizontalCenter: parent.horizontalCenter
            }

            Column {
                spacing: 10
                anchors.horizontalCenter: parent.horizontalCenter

                Row {
                    spacing: 10
                    Label {
                        text: qsTr("Launch Count:")
                        font.pixelSize: 16
                    }
                    Label {
                        text: qsTr("%1").arg(viewModel.launchCount)
                        font.pixelSize: 16
                    }
                }

                Row {
                    spacing: 10
                    Label {
                        text: qsTr("Unique ID:")
                        font.pixelSize: 16
                    }
                    Label {
                        text: qsTr("%1").arg(viewModel.uniqueId)
                        font.pixelSize: 16
                    }
                }

                Row {
                    spacing: 10
                    Label {
                        text: qsTr("Attempts:")
                        font.pixelSize: 16
                    }
                    Label {
                        text: qsTr("%1").arg(viewModel.attempts)
                        font.pixelSize: 16
                    }
                }

                Row {
                    spacing: 10
                    Label {
                        text: qsTr("Total Score:")
                        font.pixelSize: 16
                    }
                    Label {
                        text: qsTr("%1").arg(viewModel.totalScore)
                        font.pixelSize: 16
                    }
                }

                Row {
                    spacing: 10
                    Label {
                        text: qsTr("Best Score:")
                        font.pixelSize: 16
                    }
                    Label {
                        text: qsTr("%1").arg(viewModel.bestScore)
                        font.pixelSize: 16
                    }
                }
            }
        }

        Dialog {
            id: attemptsDialog
            title: qsTr("Game Over")
            standardButtons: Dialog.Ok
            onAccepted: {
                viewModel.resetGame()
            }

            Column {
                spacing: 10
                padding: 10

                Label {
                    id: attemptsDialogLabel
                    wrapMode: Text.WordWrap
                    text: ""
                }
            }
        }

        SequentialAnimation {
            id: scaleAnimation
            running: false
            loops: 1
            NumberAnimation {
                target: submitButton
                property: "scale"
                to: 0.8
                duration: 300
                easing.type: Easing.OutQuad
            }
            NumberAnimation {
                target: submitButton
                property: "scale"
                to: 1
                duration: 300
                easing.type: Easing.InQuad
            }
        }
    }
}
