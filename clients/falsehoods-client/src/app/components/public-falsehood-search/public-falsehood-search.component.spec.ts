import { ComponentFixture, TestBed } from '@angular/core/testing';
import { APP_BASE_HREF } from '@angular/common';
import { PublicFalsehoodSearchComponent } from './public-falsehood-search.component';
import { AppModule } from 'src/app/app.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SearchService } from 'src/app/services/search.service';


describe('PublicFalsehoodSearchComponent', () => {

  let component: PublicFalsehoodSearchComponent;
  let fixture: ComponentFixture<PublicFalsehoodSearchComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [PublicFalsehoodSearchComponent],
      imports: [
        HttpClientTestingModule
      ],
      providers: [{provide:APP_BASE_HREF, useValue:'/'}, SearchService]
    }).compileComponents();

    fixture = TestBed.createComponent(PublicFalsehoodSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  })
});
