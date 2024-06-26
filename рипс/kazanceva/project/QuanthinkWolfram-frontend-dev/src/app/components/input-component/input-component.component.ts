import { Component } from '@angular/core';
import { CalculationService } from "../../services/calc.service";
import { Router } from "@angular/router";
import { LanguageService } from '../../services/language.service';

@Component({
  selector: 'app-input',
  templateUrl: './input-component.component.html',
  styleUrls: ['./input-component.component.css']
})
export class InputComponent {
  selectedCalculations: string = "BA";
  inputValue: string = '';
  isInputFocused: boolean = false;
  calculationResult: string | undefined;
  
inputError: string = '';
  constructor(
    private calcService: CalculationService,
    private router: Router,
    private languageService: LanguageService
  ) { }
 ngOnInit() {
    this.languageService.languageChanged.subscribe(() => {
      this.languageChangedCallback();
    });
  }

  languageChangedCallback() {
    this.inputError = '';
  }
  onInputFocus() {
    this.isInputFocused = true;
  }

  onInputBlur() {
    setTimeout(() => {
      this.isInputFocused = false;
    }, 200);
  }

  addCharacter(character: string) {
    this.inputValue += character;
  }

  deleteLastCharacter() {
    this.inputValue = this.inputValue.slice(0, -1);
  }

  clearInput() {
    this.inputValue = '';
  }


  showResult(result: string) {
    console.log(result);
    this.calculationResult = result;
  }

getTranslation(key: string): string {
    return this.languageService.getTranslation(key);
  }

changeCalc(key: string){
  this.selectedCalculations = key;
}



}

