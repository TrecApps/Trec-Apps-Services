import { Component, OnInit } from '@angular/core';
import { Falsehood, FalsehoodSearchObject, FullFalsehood } from 'src/app/models/falsehoods';
import { FalsehoodSearchService } from 'src/app/services/falsehood-search.service';

@Component({
  selector: 'app-falsehood-search',
  templateUrl: './falsehood-search.component.html',
  styleUrls: ['./falsehood-search.component.css']
})
export class FalsehoodSearchComponent implements OnInit {

  constructor(private searcher: FalsehoodSearchService) { 
    this.falsehoods = [];
    this.falsehood = null;
  }

  ngOnInit(): void {
  }

  falsehoods: Falsehood[];
  falsehood: FullFalsehood;

  selectFalsehood(falsehood: Falsehood){
    this.searcher.getFalsehood(falsehood.id).then((value: FullFalsehood) => {
      this.falsehood = value;
    });
    
  }

  initializeList(searchObj: FalsehoodSearchObject) {
    this.searcher.searchFalsehoods(searchObj).then((value:Falsehood[])=> {
      this.falsehoods = value;
      this.falsehood = null;
    });
  }

  initializeSubmittedList(page:number, size:number) {
    this.searcher.searchSubmittedFalsehoods(size, page).then((value:Falsehood[])=> {
      this.falsehoods = value;
      this.falsehood = null;
    });
  }

  nullifyList() {
    this.falsehoods = null;
    this.falsehood = null;
  }
}
