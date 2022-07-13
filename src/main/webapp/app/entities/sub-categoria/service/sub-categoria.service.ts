import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubCategoria, getSubCategoriaIdentifier } from '../sub-categoria.model';

export type EntityResponseType = HttpResponse<ISubCategoria>;
export type EntityArrayResponseType = HttpResponse<ISubCategoria[]>;

@Injectable({ providedIn: 'root' })
export class SubCategoriaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-categorias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subCategoria: ISubCategoria): Observable<EntityResponseType> {
    return this.http.post<ISubCategoria>(this.resourceUrl, subCategoria, { observe: 'response' });
  }

  update(subCategoria: ISubCategoria): Observable<EntityResponseType> {
    return this.http.put<ISubCategoria>(`${this.resourceUrl}/${getSubCategoriaIdentifier(subCategoria) as number}`, subCategoria, {
      observe: 'response',
    });
  }

  partialUpdate(subCategoria: ISubCategoria): Observable<EntityResponseType> {
    return this.http.patch<ISubCategoria>(`${this.resourceUrl}/${getSubCategoriaIdentifier(subCategoria) as number}`, subCategoria, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubCategoria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubCategoria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubCategoriaToCollectionIfMissing(
    subCategoriaCollection: ISubCategoria[],
    ...subCategoriasToCheck: (ISubCategoria | null | undefined)[]
  ): ISubCategoria[] {
    const subCategorias: ISubCategoria[] = subCategoriasToCheck.filter(isPresent);
    if (subCategorias.length > 0) {
      const subCategoriaCollectionIdentifiers = subCategoriaCollection.map(
        subCategoriaItem => getSubCategoriaIdentifier(subCategoriaItem)!
      );
      const subCategoriasToAdd = subCategorias.filter(subCategoriaItem => {
        const subCategoriaIdentifier = getSubCategoriaIdentifier(subCategoriaItem);
        if (subCategoriaIdentifier == null || subCategoriaCollectionIdentifiers.includes(subCategoriaIdentifier)) {
          return false;
        }
        subCategoriaCollectionIdentifiers.push(subCategoriaIdentifier);
        return true;
      });
      return [...subCategoriasToAdd, ...subCategoriaCollection];
    }
    return subCategoriaCollection;
  }
}
