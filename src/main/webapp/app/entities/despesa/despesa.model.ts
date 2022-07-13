import dayjs from 'dayjs/esm';
import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';
import { User } from 'app/entities/enumerations/user.model';

export interface IDespesa {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  valor?: number | null;
  dtVcto?: dayjs.Dayjs | null;
  dtPgto?: dayjs.Dayjs | null;
  user?: User | null;
  subCategoria?: ISubCategoria | null;
}

export class Despesa implements IDespesa {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public valor?: number | null,
    public dtVcto?: dayjs.Dayjs | null,
    public dtPgto?: dayjs.Dayjs | null,
    public user?: User | null,
    public subCategoria?: ISubCategoria | null
  ) {}
}

export function getDespesaIdentifier(despesa: IDespesa): number | undefined {
  return despesa.id;
}
