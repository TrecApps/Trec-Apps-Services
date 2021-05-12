import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { FalsehoodsComponent } from './components/falsehoods/falsehoods.component';
import { InstitutionComponent } from './components/institution/institution.component';
import { RegionComponent } from './components/region/region.component';
import { MediaOutletComponent } from './components/media-outlet/media-outlet.component';
import { PublicFigureComponent } from './components/public-figure/public-figure.component';
import { PublicFalsehoodSearchComponent } from './components/public-falsehood-search/public-falsehood-search.component';
import { FalsehoodSearchComponent } from './components/falsehood-search/falsehood-search.component';

import { MarkedPipe } from './resources/marked.pipe';
import { PublicFalsehoodsComponent } from './components/public-falsehoods/public-falsehoods.component';

@NgModule({
  declarations: [
    MarkedPipe,
    AppComponent,
    WelcomeComponent,
    FalsehoodsComponent,
    InstitutionComponent,
    RegionComponent,
    MediaOutletComponent,
    PublicFigureComponent,
    PublicFalsehoodSearchComponent,
    FalsehoodSearchComponent,
    PublicFalsehoodsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
