import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import translationsBy from 'src/locale/translations.by.json';
import translationsEn from 'src/locale/translations.en.json';
import translationsRu from 'src/locale/translations.ru.json';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private defaultLanguage: string = 'en';
  private translations: { [key: string]: { [key: string]: string } } = {
    'en': translationsEn,
    'ru': translationsRu,
    'by': translationsBy
  };

  languageChanged: Subject<void> = new Subject<void>();
  public selectedLanguageChanged: Subject<string> = new Subject<string>();

  constructor() { }

  public getTranslation(key: string): string {
    const selectedLanguage = this.getLanguage();
    const translation = this.translations[selectedLanguage][key] || key;

    // console.log(`Translation for key '${key}' in language '${selectedLanguage}': ${translation}`);

    return translation;
  }

  public getLanguage(): string {
    const storedLanguage = localStorage.getItem('SelectedLanguage');
    const language = storedLanguage || this.defaultLanguage;

    // console.log(`Selected language: ${language}`);

    return language;
  }

public setLanguage(language: string): void {
  localStorage.setItem('SelectedLanguage', language);

  this.languageChanged.next(); // Notify other components about the language change
}
}
