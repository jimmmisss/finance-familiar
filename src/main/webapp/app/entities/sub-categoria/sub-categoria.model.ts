import dayjs from 'dayjs/esm';
import { IDespesa } from 'app/entities/despesa/despesa.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';

export interface ISubCategoria {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  dtCadastro?: dayjs.Dayjs | null;
  despesas?: IDespesa[] | null;
  categoria?: ICategoria | null;
}

export class SubCategoria implements ISubCategoria {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public dtCadastro?: dayjs.Dayjs | null,
    public despesas?: IDespesa[] | null,
    public categoria?: ICategoria | null
  ) {}
}

export function getSubCategoriaIdentifier(subCategoria: ISubCategoria): number | undefined {
  return subCategoria.id;
}
