import { Component } from '@angular/core';
import { CalculationService } from "../../services/calc.service";
import {  MatrixSum, Matrix, MatrixSub, MatrixMul, MatrixSystem, MatrixReverse, MatrixTranspose, MatrixMulByNum, MatrixDeterminant } from "../../interfaces/calculation";
import { Router } from "@angular/router";
import { LanguageService } from '../../services/language.service';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-matrix-component',
  templateUrl: './matrix-component.component.html',
  styleUrls: ['./matrix-component.component.css']
})
export class MatrixComponentComponent {
  MulByA: number = 1;
  MulByB: number = 1;
  inputValue: string = '';
  isInputFocused: boolean = false;
  inputError: string = '';
  ThreadCount: number = 1;
  @Output() resultEventM = new EventEmitter<string>();
  Matrix1: number[][] = [[0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0]];
  Matrix2: number[][] = [[0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0],
  [0,0,0,0,0,0,0]];
Arows:number = 3;
Acols:number = 3;
Brows:number = 3;
Bcols:number = 3;
  constructor(
    private calcService: CalculationService,
    private router: Router,
    private languageService: LanguageService
  ) { }
 ngOnInit() {
    this.languageService.languageChanged.subscribe(() => {
      this.languageChangedCallback();
    });
    this.onChangeA();
    this.onChangeB();
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


getTranslation(key: string): string {
  return this.languageService.getTranslation(key);
}

matrixSum(){
  const userId = localStorage.getItem('userId');

  const matrix1: Matrix = {
      rows: this.Arows,
      cols: this.Acols,
      data: this.Matrix1
  };
  const matrix2: Matrix = {
    rows: this.Brows,
      cols: this.Bcols,
    data: this.Matrix2
};
    const calcData: MatrixSum = {
      userId: userId,
      matrix1: matrix1,
      matrix2: matrix2,
      threadsUsed: this.ThreadCount,
      library: localStorage.getItem("Library") as string
    };

    this.calcService.createCalculationMatrixSum(calcData as MatrixSum).subscribe(
      response => {
        let result: string = "";
        for(let i = 0; i < response.data.result.size[0]; i++){
          result += "[";
          for(let j = 0; j < response.data.result.size[1]; j++){
            result += response.data.result.data[i][j] + ", "
          }
          result = result.slice(0,result.length - 2)
          result += "], ";
        }
        result = result.slice(0,result.length - 2)
        console.log(result);
        this.resultEventM.emit(result);
      },
      error => {
        this.resultEventM.emit(error.error.error);
      }
    );
  }

  matrixSub(){
    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: this.Arows,
        cols: this.Acols,
        data: this.Matrix1
    };
    const matrix2: Matrix = {
      rows: this.Brows,
        cols: this.Bcols,
      data: this.Matrix2
  };
      const calcData: MatrixSub = {
        userId: userId,
        matrix1: matrix1,
        matrix2: matrix2,
        threadsUsed: this.ThreadCount,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixSub(calcData as MatrixSub).subscribe(
        response => {
          let result: string = "";
          for(let i = 0; i < response.data.result.size[0]; i++){
            result += "[";
            for(let j = 0; j < response.data.result.size[1]; j++){
              result += response.data.result.data[i][j] + ", "
            }
            result = result.slice(0,result.length - 2)
            result += "], ";
          }
          result = result.slice(0,result.length - 2)
          console.log(result);
          this.resultEventM.emit(result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  matrixMul(){
    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: this.Arows,
        cols: this.Acols,
        data: this.Matrix1
    };
    const matrix2: Matrix = {
      rows: this.Brows,
        cols: this.Bcols,
      data: this.Matrix2
  };
      const calcData: MatrixMul = {
        userId: userId,
        matrix1: matrix1,
        matrix2: matrix2,
        threadsUsed: this.ThreadCount,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixMul(calcData as MatrixMul).subscribe(
        response => {
          let result: string = "";
          for(let i = 0; i < response.data.result.size[0]; i++){
            result += "[";
            for(let j = 0; j < response.data.result.size[1]; j++){
              result += response.data.result.data[i][j] + ", "
            }
            result = result.slice(0,result.length - 2)
            result += "], ";
          }
          result = result.slice(0,result.length - 2)
          console.log(result);
          this.resultEventM.emit(result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  matrixSystem(){
    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: this.Arows,
        cols: this.Acols,
        data: this.Matrix1
    };
    const matrix2: Matrix = {
      rows: this.Brows,
        cols: this.Bcols,
      data: this.Matrix2
  };
      const calcData: MatrixSystem = {
        userId: userId,
        matrix1: matrix1,
        matrix2: matrix2,
        threadsUsed: this.ThreadCount,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixSystem(calcData as MatrixSystem).subscribe(
        response => {
          console.log(response.data.result);
          this.resultEventM.emit(response.data.result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  clear(matrix: number[][]){
    for(let i = 0; i < matrix.length; i++)
      for(let j = 0; j < matrix[i].length; j++)
        matrix[i][j]=0;
  }

  transpose(matrix: number[][], rows: number , cols: number){

    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: rows,
        cols: cols,
        data: matrix
    };
      const calcData: MatrixTranspose = {
        userId: userId,
        matrix: matrix1,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixTranspose(calcData as MatrixTranspose).subscribe(
        response => {
          let result: string = "";
          for(let i = 0; i < response.data.result.size[0]; i++){
            result += "[";
            for(let j = 0; j < response.data.result.size[1]; j++){
              result += response.data.result.data[i][j] + ", "
            }
            result = result.slice(0,result.length - 2)
            result += "], ";
          }
          result = result.slice(0,result.length - 2)
          console.log(result);
          this.resultEventM.emit(result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  mulBy(matrix: number[][], rows: number , cols: number, num: number){
    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: rows,
        cols: cols,
        data: matrix
    };
      const calcData: MatrixMulByNum = {
        userId: userId,
        matrix: matrix1,
        number : num,
        threadsUsed: this.ThreadCount,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixMulByNum(calcData as MatrixMulByNum).subscribe(
        response => {
          let result: string = "";
          for(let i = 0; i < response.data.result.size[0]; i++){
            result += "[";
            for(let j = 0; j < response.data.result.size[1]; j++){
              result += response.data.result.data[i][j] + ", "
            }
            result = result.slice(0,result.length - 2)
            result += "], ";
          }
          result = result.slice(0,result.length - 2)
          console.log(result);
          this.resultEventM.emit(result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  determinant(matrix: number[][], rows: number , cols: number){
    const userId = localStorage.getItem('userId');

    const matrix1: Matrix = {
        rows: rows,
        cols: cols,
        data: matrix
    };
      const calcData: MatrixDeterminant = {
        userId: userId,
        matrix: matrix1,
        threadsUsed: this.ThreadCount,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixDeterminant(calcData as MatrixDeterminant).subscribe(
        response => {
          console.log(response.data.result);
          this.resultEventM.emit(response.data.result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  reverse(matrix: number[][], rows: number , cols: number){
    const userId = localStorage.getItem('userId');
    const matrix1: Matrix = {
        rows: rows,
        cols: cols,
        data: matrix
    };
      const calcData: MatrixReverse = {
        userId: userId,
        matrix: matrix1,
        library: localStorage.getItem("Library") as string
      };

      this.calcService.createCalculationMatrixReverse(calcData as MatrixReverse).subscribe(
        response => {
          let result: string = "";
          for(let i = 0; i < response.data.result.size[0]; i++){
            result += "[";
            for(let j = 0; j < response.data.result.size[1]; j++){
              result += response.data.result.data[i][j] + ", "
            }
            result = result.slice(0,result.length - 2)
            result += "], ";
          }
          result = result.slice(0,result.length - 2)
          console.log(result);
          this.resultEventM.emit(result);
        },
        error => {
          this.resultEventM.emit(error.error.error);
        }
      );
  }

  onChangeA(){
    const inputs = document.getElementsByTagName("app-input")[0].getElementsByTagName("input");
    for(let i = 0; i < inputs.length; i ++)
      if (inputs[i].getAttribute("id") != null)
        if(inputs[i].getAttribute("id")![0] == "A")
          if(Number(inputs[i].getAttribute("id")![1]) >= this.Arows || Number(inputs[i].getAttribute("id")![2]) >= this.Acols)
          inputs[i].style.display= "none";
        else
          inputs[i].style.display= "";
  }

  onChangeB(){
    const inputs = document.getElementsByTagName("app-input")[0].getElementsByTagName("input");
    for(let i = 0; i < inputs.length; i ++)
      if (inputs[i].getAttribute("id") != null)
        if(inputs[i].getAttribute("id")![0] == "B")
          if(Number(inputs[i].getAttribute("id")![1]) >= this.Brows || Number(inputs[i].getAttribute("id")![2]) >= this.Bcols)
          inputs[i].style.display= "none";
        else
          inputs[i].style.display= "";
  }
}
