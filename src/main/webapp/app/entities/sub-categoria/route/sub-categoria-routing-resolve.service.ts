import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubCategoria, SubCategoria } from '../sub-categoria.model';
import { SubCategoriaService } from '../service/sub-categoria.service';

@Injectable({ providedIn: 'root' })
export class SubCategoriaRoutingResolveService implements Resolve<ISubCategoria> {
  constructor(protected service: SubCategoriaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubCategoria> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subCategoria: HttpResponse<SubCategoria>) => {
          if (subCategoria.body) {
            return of(subCategoria.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubCategoria());
  }
}
