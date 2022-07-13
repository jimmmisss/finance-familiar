import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubCategoriaDetailComponent } from './sub-categoria-detail.component';

describe('SubCategoria Management Detail Component', () => {
  let comp: SubCategoriaDetailComponent;
  let fixture: ComponentFixture<SubCategoriaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubCategoriaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ subCategoria: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SubCategoriaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SubCategoriaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load subCategoria on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.subCategoria).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
