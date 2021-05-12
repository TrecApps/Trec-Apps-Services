import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicFalsehoodsComponent } from './public-falsehoods.component';

describe('PublicFalsehoodsComponent', () => {
  let component: PublicFalsehoodsComponent;
  let fixture: ComponentFixture<PublicFalsehoodsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicFalsehoodsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicFalsehoodsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
