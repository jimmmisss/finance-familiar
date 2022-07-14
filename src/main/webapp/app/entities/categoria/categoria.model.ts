import dayjs from 'dayjs/esm';
import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';

export interface ICategoria {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  dtCadastro?: dayjs.Dayjs | null;
  subCategorias?: ISubCategoria[] | null;
}

export class Categoria implements ICategoria {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public dtCadastro?: dayjs.Dayjs | null,
    public subCategorias?: ISubCategoria[] | null
  ) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
