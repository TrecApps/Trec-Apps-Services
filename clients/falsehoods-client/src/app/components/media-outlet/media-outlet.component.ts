import { assertNotNull } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FalsehoodSearchObject } from 'src/app/models/falsehoods';
import { MediaOutlet, MediaOutletEntry } from 'src/app/models/mediaOutlet';
import { ResourceSearchService } from 'src/app/services/resource-search.service';

@Component({
  selector: 'app-media-outlet',
  templateUrl: './media-outlet.component.html',
  styleUrls: ['./media-outlet.component.css']
})
export class MediaOutletComponent implements OnInit {

  mainOutlet: MediaOutletEntry;
  mode: Number;
  editYear:number;

  createNew: boolean;

  editName: String;
  editContents: String;

  searchText: String;
  searchOutlets: MediaOutlet[];

  //@ViewChild(FalsehoodSearchComponent) searchComponent: FalsehoodSearchComponent;

  constructor(private resourceSearch: ResourceSearchService) {

    this.searchOutlets = [];
    this.searchText = this.editContents = this.editName = "";

    this.mainOutlet = new MediaOutletEntry();
    this.mode = 0;
    this.createNew = false;

    this.editYear = 2000;

    // this.searchComponent = searchComponent;
   }

  ngOnInit(): void {
  }

  setMode(m: Number) {
    this.mode = m;

    // if(this.mode == 2 && this.mainOutlet.outlet?.outletId) {
    //   let searchObj = new FalsehoodSearchObject();
    //   searchObj.outlet = this.mainOutlet.outlet;
    //   console.log("SearchComponent is on SetCall " + this.searchComponent);
    //   console.log("edit year is " + this.editYear);
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
    let p = this.resourceSearch.searchMediaOutlets(event.target.value)
    p.then((outlets: MediaOutlet[])=> {
      this.searchOutlets = outlets;
    });
  }

  async getOutlet(id: Number) {
    let p = this.resourceSearch.getMediaOutlet(id);
    p.then((outlet: MediaOutletEntry)=> {
      this.mainOutlet = outlet;
    });

    this.searchText = "";
  }

}
