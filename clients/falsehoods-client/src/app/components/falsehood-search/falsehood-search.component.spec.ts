import { APP_BASE_HREF } from '@angular/common';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppModule } from 'src/app/app.module';
import { SearchService } from 'src/app/services/search.service';

import { FalsehoodSearchComponent } from './falsehood-search.component';

describe('FalsehoodSearchComponent', () => {
  let component: FalsehoodSearchComponent;
  let fixture: ComponentFixture<FalsehoodSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations:[
        FalsehoodSearchComponent
      ],
      imports: [
        AppModule
        ],
      providers: [ {provide: APP_BASE_HREF, useValue : '/' }, SearchService
      ]
  })
  .compileComponents();
  fixture = TestBed.createComponent(FalsehoodSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
