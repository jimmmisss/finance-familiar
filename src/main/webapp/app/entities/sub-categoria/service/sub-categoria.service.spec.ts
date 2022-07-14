import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubCategoria, SubCategoria } from '../sub-categoria.model';

import { SubCategoriaService } from './sub-categoria.service';

describe('SubCategoria Service', () => {
  let service: SubCategoriaService;
  let httpMock: HttpTestingController;
  let elemDefault: ISubCategoria;
  let expectedResult: ISubCategoria | ISubCategoria[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubCategoriaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
      descricao: 'AAAAAAA',
      dtCadastro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dtCadastro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SubCategoria', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dtCadastro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dtCadastro: currentDate,
        },
        returnedFromService
      );

      service.create(new SubCategoria()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubCategoria', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          descricao: 'BBBBBB',
          dtCadastro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dtCadastro: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubCategoria', () => {
      const patchObject = Object.assign({}, new SubCategoria());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dtCadastro: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubCategoria', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          descricao: 'BBBBBB',
          dtCadastro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dtCadastro: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SubCategoria', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSubCategoriaToCollectionIfMissing', () => {
      it('should add a SubCategoria to an empty array', () => {
        const subCategoria: ISubCategoria = { id: 123 };
        expectedResult = service.addSubCategoriaToCollectionIfMissing([], subCategoria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subCategoria);
      });

      it('should not add a SubCategoria to an array that contains it', () => {
        const subCategoria: ISubCategoria = { id: 123 };
        const subCategoriaCollection: ISubCategoria[] = [
          {
            ...subCategoria,
          },
          { id: 456 },
        ];
        expectedResult = service.addSubCategoriaToCollectionIfMissing(subCategoriaCollection, subCategoria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubCategoria to an array that doesn't contain it", () => {
        const subCategoria: ISubCategoria = { id: 123 };
        const subCategoriaCollection: ISubCategoria[] = [{ id: 456 }];
        expectedResult = service.addSubCategoriaToCollectionIfMissing(subCategoriaCollection, subCategoria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subCategoria);
      });

      it('should add only unique SubCategoria to an array', () => {
        const subCategoriaArray: ISubCategoria[] = [{ id: 123 }, { id: 456 }, { id: 30377 }];
        const subCategoriaCollection: ISubCategoria[] = [{ id: 123 }];
        expectedResult = service.addSubCategoriaToCollectionIfMissing(subCategoriaCollection, ...subCategoriaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subCategoria: ISubCategoria = { id: 123 };
        const subCategoria2: ISubCategoria = { id: 456 };
        expectedResult = service.addSubCategoriaToCollectionIfMissing([], subCategoria, subCategoria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subCategoria);
        expect(expectedResult).toContain(subCategoria2);
      });

      it('should accept null and undefined values', () => {
        const subCategoria: ISubCategoria = { id: 123 };
        expectedResult = service.addSubCategoriaToCollectionIfMissing([], null, subCategoria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subCategoria);
      });

      it('should return initial array if no SubCategoria is added', () => {
        const subCategoriaCollection: ISubCategoria[] = [{ id: 123 }];
        expectedResult = service.addSubCategoriaToCollectionIfMissing(subCategoriaCollection, undefined, null);
        expect(expectedResult).toEqual(subCategoriaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
