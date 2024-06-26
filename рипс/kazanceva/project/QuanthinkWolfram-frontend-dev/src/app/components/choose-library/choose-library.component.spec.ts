import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseLibraryComponent } from './choose-library.component';

describe('ChooseLibraryComponent', () => {
  let component: ChooseLibraryComponent;
  let fixture: ComponentFixture<ChooseLibraryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChooseLibraryComponent]
    });
    fixture = TestBed.createComponent(ChooseLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
