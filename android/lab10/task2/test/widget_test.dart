// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:task3/main.dart';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_map/flutter_map.dart';

void main() {
  testWidgets('Displays map with university markers', (WidgetTester tester) async {
    // Загружаем приложение
    await tester.pumpWidget(BrestUniversitiesMapApp());

    // Проверяем наличие карты
    expect(find.byType(FlutterMap), findsOneWidget);

    // Проверяем наличие маркеров
    expect(find.byIcon(Icons.location_on), findsNWidgets(2));
  });

  testWidgets('Displays dialog on marker tap', (WidgetTester tester) async {
    // Загружаем приложение
    await tester.pumpWidget(BrestUniversitiesMapApp());

    // Имитируем нажатие на маркер
    await tester.tap(find.byIcon(Icons.location_on).first);
    await tester.pumpAndSettle();

    // Проверяем, что открылось диалоговое окно
    expect(find.byType(AlertDialog), findsOneWidget);

    // Проверяем наличие текста в диалоговом окне
    expect(find.text('Brest State University'), findsOneWidget);
  });

  // testWidgets('Marker animates on tap', (WidgetTester tester) async {
  //   // Загружаем приложение
  //   await tester.pumpWidget(BrestUniversitiesMapApp());
  //
  //   // Имитируем нажатие на маркер
  //   await tester.tap(find.byIcon(Icons.location_on).first);
  //   await tester.pump();
  //
  //   // Проверяем, что началась анимация
  //   final scaleTransitionFinder = find.byType(ScaleTransition);
  //   final scaleTransition = tester.widget<ScaleTransition>(scaleTransitionFinder.first);
  //   expect(scaleTransition.scale.value, greaterThan(1.0));
  //
  //   // Завершаем анимацию
  //   await tester.pumpAndSettle();
  //
  //   // Проверяем, что анимация завершилась
  //   expect(scaleTransition.scale.value, equals(1.0));
  // });
}

