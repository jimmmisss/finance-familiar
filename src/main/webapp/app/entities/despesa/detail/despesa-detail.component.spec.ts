import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DespesaDetailComponent } from './despesa-detail.component';

describe('Despesa Management Detail Component', () => {
  let comp: DespesaDetailComponent;
  let fixture: ComponentFixture<DespesaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DespesaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ despesa: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DespesaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DespesaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load despesa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.despesa).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
