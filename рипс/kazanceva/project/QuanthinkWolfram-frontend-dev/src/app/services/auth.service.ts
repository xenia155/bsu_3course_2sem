import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/auth';
import { User_login } from '../interfaces/user_login';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registerUser(userDetails: User): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, userDetails);
  }

  login(authRequest: User_login): Observable<any> {
    return this.http.post<User>(`${this.baseUrl}/login`, authRequest);
  }

  logout(): Observable<any> {
    return this.http.post(`${this.baseUrl}/logout`, null);
  }
}
