import { Component, OnInit } from '@angular/core';
import { FullPublicFalsehood, PublicFalsehood, SearchPublicFalsehood } from 'src/app/models/publicFalsehood';
import { FalsehoodSearchService } from 'src/app/services/falsehood-search.service';


@Component({
  selector: 'app-public-falsehood-search',
  templateUrl: './public-falsehood-search.component.html',
  styleUrls: ['./public-falsehood-search.component.css']
})
export class PublicFalsehoodSearchComponent implements OnInit {

  constructor(private searcher: FalsehoodSearchService) { 
    this.falsehoods = [];
    this.falsehood = null;
  }

  ngOnInit(): void {
  }

  falsehoods: PublicFalsehood[];
  falsehood: FullPublicFalsehood;

  selectFalsehood(falsehood: PublicFalsehood){
    this.searcher.getPublicFalsehood(falsehood.id).then((full: FullPublicFalsehood) => {
      this.falsehood = full;

    });
    
  }

  initializeList(searchObj: SearchPublicFalsehood) {
    this.searcher.searchPublicFalsehoods(searchObj).then((value:PublicFalsehood[])=> {
      console.log("Retrieved Values of {}", value);
      this.falsehoods = value;
      this.falsehood = null;
    }).catch((reason)=> {

    });

  }



  initializeSubmittedList(page:number, size:number) {
    this.searcher.searchSubmittedPublicFalsehoods(size, page).then((value:PublicFalsehood[])=> {
      this.falsehoods = value;
      this.falsehood = null;
    });
  }

  nullifyList() {
    this.falsehoods = null;
    this.falsehood = null;
  }
}
