import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquationComponentComponent } from './equation-component.component';

describe('EquationComponentComponent', () => {
  let component: EquationComponentComponent;
  let fixture: ComponentFixture<EquationComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EquationComponentComponent]
    });
    fixture = TestBed.createComponent(EquationComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
