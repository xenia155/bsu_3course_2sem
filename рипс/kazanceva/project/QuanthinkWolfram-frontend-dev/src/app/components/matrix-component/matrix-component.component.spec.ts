import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatrixComponentComponent } from './matrix-component.component';

describe('MatrixComponentComponent', () => {
  let component: MatrixComponentComponent;
  let fixture: ComponentFixture<MatrixComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatrixComponentComponent]
    });
    fixture = TestBed.createComponent(MatrixComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
