import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { RegionComponent } from './components/region/region.component';
import { InstitutionComponent } from './components/institution/institution.component';
import { FigureComponent } from './components/figure/figure.component';
import { OutletComponent } from './components/outlet/outlet.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    RegionComponent,
    InstitutionComponent,
    FigureComponent,
    OutletComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
