import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FalsehoodsComponent } from './falsehoods.component';

describe('FalsehoodsComponent', () => {
  let component: FalsehoodsComponent;
  let fixture: ComponentFixture<FalsehoodsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FalsehoodsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FalsehoodsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
