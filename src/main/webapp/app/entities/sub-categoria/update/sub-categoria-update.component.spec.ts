import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubCategoriaService } from '../service/sub-categoria.service';
import { ISubCategoria, SubCategoria } from '../sub-categoria.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

import { SubCategoriaUpdateComponent } from './sub-categoria-update.component';

describe('SubCategoria Management Update Component', () => {
  let comp: SubCategoriaUpdateComponent;
  let fixture: ComponentFixture<SubCategoriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subCategoriaService: SubCategoriaService;
  let categoriaService: CategoriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SubCategoriaUpdateComponent],
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
      .overrideTemplate(SubCategoriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubCategoriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subCategoriaService = TestBed.inject(SubCategoriaService);
    categoriaService = TestBed.inject(CategoriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categoria query and add missing value', () => {
      const subCategoria: ISubCategoria = { id: 456 };
      const categoria: ICategoria = { id: 91487 };
      subCategoria.categoria = categoria;

      const categoriaCollection: ICategoria[] = [{ id: 83843 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [categoria];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subCategoria });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(categoriaCollection, ...additionalCategorias);
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subCategoria: ISubCategoria = { id: 456 };
      const categoria: ICategoria = { id: 13357 };
      subCategoria.categoria = categoria;

      activatedRoute.data = of({ subCategoria });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(subCategoria));
      expect(comp.categoriasSharedCollection).toContain(categoria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCategoria>>();
      const subCategoria = { id: 123 };
      jest.spyOn(subCategoriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCategoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subCategoria }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(subCategoriaService.update).toHaveBeenCalledWith(subCategoria);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCategoria>>();
      const subCategoria = new SubCategoria();
      jest.spyOn(subCategoriaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCategoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subCategoria }));
      saveSubject.complete();

      // THEN
      expect(subCategoriaService.create).toHaveBeenCalledWith(subCategoria);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SubCategoria>>();
      const subCategoria = { id: 123 };
      jest.spyOn(subCategoriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subCategoria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subCategoriaService.update).toHaveBeenCalledWith(subCategoria);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoriaById', () => {
      it('Should return tracked Categoria primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoriaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
