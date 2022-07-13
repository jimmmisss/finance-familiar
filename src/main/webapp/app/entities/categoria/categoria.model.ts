import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';

export interface ICategoria {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  subCategorias?: ISubCategoria[] | null;
}

export class Categoria implements ICategoria {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public subCategorias?: ISubCategoria[] | null
  ) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
