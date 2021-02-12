import { Component, OnInit, ViewChild } from '@angular/core';
import { FalsehoodSearchObject } from 'src/app/models/falsehoods';
import { SearchPublicFalsehood } from 'src/app/models/publicFalsehood';
import { PublicFigure, PublicFigureEntry } from 'src/app/models/publicFigure';
import { ResourceSearchService } from 'src/app/services/resource-search.service';

@Component({
  selector: 'app-public-figure',
  templateUrl: './public-figure.component.html',
  styleUrls: ['./public-figure.component.css']
})
export class PublicFigureComponent implements OnInit {

  mainFigure: PublicFigureEntry;

  mode: Number;

  createNew: boolean;

  editName: String;
  editContents: String;

  searchText: String;
  searchFigures: PublicFigure[];

  //@ViewChild(PublicFalsehoodSearchComponent) publicSearchComponent: PublicFalsehoodSearchComponent;
  //@ViewChild(FalsehoodSearchComponent) searchComponent: FalsehoodSearchComponent;
  constructor(private resourceSearch: ResourceSearchService) { 

    this.searchFigures = [];
    this.searchText = "";
    this.editContents = this.editName = "";

    this.mainFigure = new PublicFigureEntry();

    this.mode = 0;
    this.createNew = false;

    //this.searchComponent = new FalsehoodSearchComponent(search);
    //this.publicSearchComponent = new PublicFalsehoodSearchComponent(search);
  }

  ngOnInit(): void {
  }

  setMode(m: Number) {
    this.mode = m;

    // if(this.mode == 2 && this.mainFigure) {
    //   let searchObj = new SearchPublicFalsehood();
    //   searchObj.official = this.mainFigure.figure;

    //   this.publicSearchComponent.initializeList(searchObj);
    // } else if(this.mode == 3 && this.mainFigure) {
    //   let searchObj = new FalsehoodSearchObject();
    //   searchObj.author = this.mainFigure.figure;
    //   this.searchComponent.initializeList(searchObj);
    // }

  }

  stopCreateNew() {
    this.editContents = "";
    this.editName = "";
    this.createNew=false;
  }


  startCreateNew() {
    this.createNew = true;
  }

  async onSearchUpdate(event:any){
    let p = this.resourceSearch.searchPublicFigures(event.target.value)
    p.then((figures: PublicFigure[])=> {
      this.searchFigures = figures;
    });
  }

  async getFigure(id: Number) {
    let p = this.resourceSearch.getPublicFigure(id);
    p.then((figure: PublicFigureEntry)=> {
      this.mainFigure = figure;
    });

    this.searchText = "";
  }

}
