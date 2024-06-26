import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { HttpClientModule } from '@angular/common/http';
import { ToastModule } from 'primeng/toast';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MessageService } from 'primeng/api';
import { InputComponent } from './components/input-component/input-component.component';
import { FormsModule } from '@angular/forms';
 import { ReactiveFormsModule } from '@angular/forms';
import { ChooseLibraryComponent } from './components/choose-library/choose-library.component';
import { HistoryComponent } from './components/history/history.component';
import { SettingsComponent } from './components/settings/settings.component';
import { OnlineChatComponent } from './components/online-chat/online-chat.component';
import { LanguageSwitchComponent } from './components/language-switch/language-switch.component';
import { BasicArithemticComponentComponent } from './components/basic-arithemtic-component/basic-arithemtic-component.component';
import { EquationComponentComponent } from './components/equation-component/equation-component.component';
import { MatrixComponentComponent } from './components/matrix-component/matrix-component.component';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    InputComponent,
    ChooseLibraryComponent,
    HistoryComponent,
    SettingsComponent,
    OnlineChatComponent,
    LanguageSwitchComponent,
    BasicArithemticComponentComponent,
    EquationComponentComponent,
    MatrixComponentComponent
  ],
  imports: [
ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    CardModule,
    InputTextModule,
    FormsModule, // Добавлен FormsModule
    ButtonModule,
    HttpClientModule,
    ToastModule,
    BrowserAnimationsModule
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }