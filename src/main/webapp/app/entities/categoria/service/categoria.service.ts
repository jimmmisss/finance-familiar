import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoria, getCategoriaIdentifier } from '../categoria.model';

export type EntityResponseType = HttpResponse<ICategoria>;
export type EntityArrayResponseType = HttpResponse<ICategoria[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categorias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoria: ICategoria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categoria);
    return this.http
      .post<ICategoria>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(categoria: ICategoria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categoria);
    return this.http
      .put<ICategoria>(`${this.resourceUrl}/${getCategoriaIdentifier(categoria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(categoria: ICategoria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categoria);
    return this.http
      .patch<ICategoria>(`${this.resourceUrl}/${getCategoriaIdentifier(categoria) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICategoria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICategoria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategoriaToCollectionIfMissing(
    categoriaCollection: ICategoria[],
    ...categoriasToCheck: (ICategoria | null | undefined)[]
  ): ICategoria[] {
    const categorias: ICategoria[] = categoriasToCheck.filter(isPresent);
    if (categorias.length > 0) {
      const categoriaCollectionIdentifiers = categoriaCollection.map(categoriaItem => getCategoriaIdentifier(categoriaItem)!);
      const categoriasToAdd = categorias.filter(categoriaItem => {
        const categoriaIdentifier = getCategoriaIdentifier(categoriaItem);
        if (categoriaIdentifier == null || categoriaCollectionIdentifiers.includes(categoriaIdentifier)) {
          return false;
        }
        categoriaCollectionIdentifiers.push(categoriaIdentifier);
        return true;
      });
      return [...categoriasToAdd, ...categoriaCollection];
    }
    return categoriaCollection;
  }

  protected convertDateFromClient(categoria: ICategoria): ICategoria {
    return Object.assign({}, categoria, {
      dtCadastro: categoria.dtCadastro?.isValid() ? categoria.dtCadastro.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dtCadastro = res.body.dtCadastro ? dayjs(res.body.dtCadastro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((categoria: ICategoria) => {
        categoria.dtCadastro = categoria.dtCadastro ? dayjs(categoria.dtCadastro) : undefined;
      });
    }
    return res;
  }
}
