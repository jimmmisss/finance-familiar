import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DespesaService } from '../service/despesa.service';
import { IDespesa, Despesa } from '../despesa.model';
import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';
import { SubCategoriaService } from 'app/entities/sub-categoria/service/sub-categoria.service';

import { DespesaUpdateComponent } from './despesa-update.component';

describe('Despesa Management Update Component', () => {
  let comp: DespesaUpdateComponent;
  let fixture: ComponentFixture<DespesaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let despesaService: DespesaService;
  let subCategoriaService: SubCategoriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DespesaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DespesaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DespesaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    despesaService = TestBed.inject(DespesaService);
    subCategoriaService = TestBed.inject(SubCategoriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SubCategoria query and add missing value', () => {
      const despesa: IDespesa = { id: 456 };
      const subCategoria: ISubCategoria = { id: 54993 };
      despesa.subCategoria = subCategoria;

      const subCategoriaCollection: ISubCategoria[] = [{ id: 45816 }];
      jest.spyOn(subCategoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: subCategoriaCollection })));
      const additionalSubCategorias = [subCategoria];
      const expectedCollection: ISubCategoria[] = [...additionalSubCategorias, ...subCategoriaCollection];
      jest.spyOn(subCategoriaService, 'addSubCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ despesa });
      comp.ngOnInit();

      expect(subCategoriaService.query).toHaveBeenCalled();
      expect(subCategoriaService.addSubCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        subCategoriaCollection,
        ...additionalSubCategorias
      );
      expect(comp.subCategoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const despesa: IDespesa = { id: 456 };
      const subCategoria: ISubCategoria = { id: 38630 };
      despesa.subCategoria = subCategoria;

      activatedRoute.data = of({ despesa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(despesa));
      expect(comp.subCategoriasSharedCollection).toContain(subCategoria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Despesa>>();
      const despesa = { id: 123 };
      jest.spyOn(despesaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despesa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: despesa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(despesaService.update).toHaveBeenCalledWith(despesa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Despesa>>();
      const despesa = new Despesa();
      jest.spyOn(despesaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despesa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: despesa }));
      saveSubject.complete();

      // THEN
      expect(despesaService.create).toHaveBeenCalledWith(despesa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Despesa>>();
      const despesa = { id: 123 };
      jest.spyOn(despesaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despesa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(despesaService.update).toHaveBeenCalledWith(despesa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSubCategoriaById', () => {
      it('Should return tracked SubCategoria primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSubCategoriaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
