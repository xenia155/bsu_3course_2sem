import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:http/http.dart' as http;
import 'package:latlong2/latlong.dart';

void main() {
  runApp(BrestUniversitiesMapApp());
}

class BrestUniversitiesMapApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Brest Universities Map',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: UniversitiesMapScreen(),
    );
  }
}

class UniversitiesMapScreen extends StatefulWidget {
  @override
  _UniversitiesMapScreenState createState() => _UniversitiesMapScreenState();
}

class _UniversitiesMapScreenState extends State<UniversitiesMapScreen> {
  final List<LatLng> universityLocations = [
    LatLng(52.097621, 23.734051), // Brest State University
    LatLng(52.094585, 23.734720), // Brest Polytechnic College
    // Add more coordinates here
  ];

  final List<String> universityNames = [
    "Brest State University",
    "Brest Polytechnic College",
    // Add more names here
  ];

  final String apiKey = '64dcd64a8bc1d61acbdc689214252155';
  Map<String, dynamic> weatherData = {};

  @override
  void initState() {
    super.initState();
    _fetchWeatherData();
  }

  Future<void> _fetchWeatherData() async {
    for (int i = 0; i < universityLocations.length; i++) {
      final lat = universityLocations[i].latitude;
      final lon = universityLocations[i].longitude;
      final response = await http.get(
        Uri.parse(
          'https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric',
        ),
      );
      if (response.statusCode == 200) {
        setState(() {
          weatherData[universityNames[i]] = json.decode(response.body);
        });
      } else {
        print('Failed to load weather data');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Brest Universities Map'),
      ),
      body: FlutterMap(
        options: MapOptions(
          center: LatLng(52.097621, 23.734051),
          zoom: 13,
          onTap: (tapPosition, latlng) => _handleTap(latlng),
        ),
        children: [
          TileLayer(
            urlTemplate: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
            subdomains: ['a', 'b', 'c'],
          ),
          MarkerLayer(
            markers: _buildMarkers(),
          ),
        ],
      ),
    );
  }

  List<Marker> _buildMarkers() {
    List<Marker> markers = [];
    for (int i = 0; i < universityLocations.length; i++) {
      markers.add(
        Marker(
          width: 80.0,
          height: 80.0,
          point: universityLocations[i],
          builder: (ctx) => AnimatedMarker(
            universityName: universityNames[i],
            onTap: () {
              _showUniversityDetails(context, universityNames[i]);
            },
          ),
        ),
      );
    }
    return markers;
  }

  void _showUniversityDetails(BuildContext context, String name) {
    final weather = weatherData[name];
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(name),
          content: weather != null
              ? Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text("Temperature: ${weather['main']['temp']} °C"),
              Text("Weather: ${weather['weather'][0]['description']}"),
            ],
          )
              : Text("No weather data available."),
          actions: <Widget>[
            TextButton(
              child: Text("Close"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  void _handleTap(LatLng latlng) {
    // You can handle map taps here if needed
  }
}

class AnimatedMarker extends StatefulWidget {
  final String universityName;
  final VoidCallback onTap;

  const AnimatedMarker({
    required this.universityName,
    required this.onTap,
  });

  @override
  _AnimatedMarkerState createState() => _AnimatedMarkerState();
}

class _AnimatedMarkerState extends State<AnimatedMarker> with SingleTickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      duration: const Duration(milliseconds: 500),
      vsync: this,
    );

    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.2).animate(
      CurvedAnimation(
        parent: _animationController,
        curve: Curves.easeInOut,
      ),
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        widget.onTap();
        _animationController.forward().then((_) {
          _animationController.reverse();
        });
      },
      child: AnimatedBuilder(
        animation: _animationController,
        builder: (context, child) {
          return Transform.scale(
            scale: _scaleAnimation.value,
            child: child,
          );
        },
        child: Icon(
          Icons.location_on,
          color: Colors.red,
          size: 45.0,
        ),
      ),
    );
  }
}
