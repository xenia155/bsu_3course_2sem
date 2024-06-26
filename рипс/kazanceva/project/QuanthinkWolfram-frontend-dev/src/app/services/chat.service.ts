import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

import { Message } from '../interfaces/message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/messages';
  private socket$: WebSocketSubject<Message>;

  constructor(private http: HttpClient) {
    this.socket$ = webSocket<Message>('ws://localhost:8080/chat');
  }

  getMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(this.apiUrl);
  }

  sendMessage(message: Message): void {
    this.socket$.next(message);
  }

  receiveMessages(): Observable<Message> {
    return this.socket$;
  }
}