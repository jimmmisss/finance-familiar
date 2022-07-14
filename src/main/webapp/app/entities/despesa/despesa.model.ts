import dayjs from 'dayjs/esm';
import { ISubCategoria } from 'app/entities/sub-categoria/sub-categoria.model';
import { Mes } from 'app/entities/enumerations/mes.model';
import { FormaPgto } from 'app/entities/enumerations/forma-pgto.model';
import { Responsavel } from 'app/entities/enumerations/responsavel.model';

export interface IDespesa {
  id?: number;
  mes?: Mes | null;
  nome?: string | null;
  descricao?: string | null;
  valor?: number | null;
  dtVcto?: dayjs.Dayjs | null;
  dtPgto?: dayjs.Dayjs | null;
  formaPgto?: FormaPgto | null;
  responsavel?: Responsavel | null;
  dtCadastro?: dayjs.Dayjs | null;
  subCategoria?: ISubCategoria | null;
}

export class Despesa implements IDespesa {
  constructor(
    public id?: number,
    public mes?: Mes | null,
    public nome?: string | null,
    public descricao?: string | null,
    public valor?: number | null,
    public dtVcto?: dayjs.Dayjs | null,
    public dtPgto?: dayjs.Dayjs | null,
    public formaPgto?: FormaPgto | null,
    public responsavel?: Responsavel | null,
    public dtCadastro?: dayjs.Dayjs | null,
    public subCategoria?: ISubCategoria | null
  ) {}
}

export function getDespesaIdentifier(despesa: IDespesa): number | undefined {
  return despesa.id;
}
