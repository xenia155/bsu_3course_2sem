import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicArithemticComponentComponent } from './basic-arithemtic-component.component';

describe('BasicArithemticComponentComponent', () => {
  let component: BasicArithemticComponentComponent;
  let fixture: ComponentFixture<BasicArithemticComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BasicArithemticComponentComponent]
    });
    fixture = TestBed.createComponent(BasicArithemticComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
