import { TestBed } from '@angular/core/testing';

import { HomeinsuranceService } from './homeinsurance.service';

describe('HomeinsuranceService', () => {
  let service: HomeinsuranceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HomeinsuranceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
