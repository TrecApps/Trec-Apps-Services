import { TestBed } from '@angular/core/testing';

import { FalsehoodSearchService } from './falsehood-search.service';

describe('FalsehoodSearchService', () => {
  let service: FalsehoodSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FalsehoodSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
