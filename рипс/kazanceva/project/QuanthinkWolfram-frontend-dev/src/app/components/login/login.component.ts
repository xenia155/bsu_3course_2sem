import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LanguageService } from '../../services/language.service';
import { Subscription } from 'rxjs';
import { EventEmitter } from '@angular/core';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  host: { class: 'orange-form' }
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
inputError: string = '';
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private msgService: MessageService,
    private languageService: LanguageService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
this.languageService.languageChanged.subscribe(() => {
      this.languageChangedCallback();
    });
    if (sessionStorage.getItem('email')) {
      this.router.navigate(['/']);
    }
}

languageChangedCallback() {
  this.inputError = this.languageService.getTranslation('Input Error');
  console.log('Current language:', this.languageService.getLanguage());
  console.log('Translated input error:', this.inputError);
}

getTranslation(key: string): string {
    return this.languageService.getTranslation(key);
  }



  get email() {
    return this.loginForm.controls['email'];
  }

  get password() {
    return this.loginForm.controls['password'];
  }

  loginUser() {
    const { email, password } = this.loginForm.value;
    this.authService.login({ email, password }).subscribe(
      response => {
        console.log(response);
        localStorage.setItem('userId', response.data.id);
        localStorage.setItem('email', response.data.email);
        localStorage.setItem('tokenType', response.data.type);
        localStorage.setItem('token', response.data.token);
        this.msgService.add({ severity: 'success', summary: 'Success', detail: 'Login successful' });
        this.router.navigate(['/home']);
      },
      error => {
        this.msgService.add({ severity: 'error', summary: 'Error', detail: 'Incorrect email or password' });
      }
    );
  }

}
