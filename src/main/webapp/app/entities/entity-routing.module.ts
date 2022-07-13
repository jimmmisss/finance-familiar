import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'categoria',
        data: { pageTitle: 'Categorias' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'sub-categoria',
        data: { pageTitle: 'SubCategorias' },
        loadChildren: () => import('./sub-categoria/sub-categoria.module').then(m => m.SubCategoriaModule),
      },
      {
        path: 'despesa',
        data: { pageTitle: 'Despesas' },
        loadChildren: () => import('./despesa/despesa.module').then(m => m.DespesaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
