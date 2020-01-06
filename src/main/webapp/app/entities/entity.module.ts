import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'endereco',
        loadChildren: () => import('./endereco/endereco.module').then(m => m.CinematicaEnderecoModule)
      },
      {
        path: 'empresa',
        loadChildren: () => import('./empresa/empresa.module').then(m => m.CinematicaEmpresaModule)
      },
      {
        path: 'profissao',
        loadChildren: () => import('./profissao/profissao.module').then(m => m.CinematicaProfissaoModule)
      },
      {
        path: 'forma-pagamento',
        loadChildren: () => import('./forma-pagamento/forma-pagamento.module').then(m => m.CinematicaFormaPagamentoModule)
      },
      {
        path: 'especialidade',
        loadChildren: () => import('./especialidade/especialidade.module').then(m => m.CinematicaEspecialidadeModule)
      },
      {
        path: 'medico',
        loadChildren: () => import('./medico/medico.module').then(m => m.CinematicaMedicoModule)
      },
      {
        path: 'paciente',
        loadChildren: () => import('./paciente/paciente.module').then(m => m.CinematicaPacienteModule)
      },
      {
        path: 'fluxo-caixa',
        loadChildren: () => import('./fluxo-caixa/fluxo-caixa.module').then(m => m.CinematicaFluxoCaixaModule)
      },
      {
        path: 'horario',
        loadChildren: () => import('./horario/horario.module').then(m => m.CinematicaHorarioModule)
      },
      {
        path: 'horario-disponivel',
        loadChildren: () => import('./horario-disponivel/horario-disponivel.module').then(m => m.CinematicaHorarioDisponivelModule)
      },
      {
        path: 'data-falta',
        loadChildren: () => import('./data-falta/data-falta.module').then(m => m.CinematicaDataFaltaModule)
      },
      {
        path: 'configuracao-agenda',
        loadChildren: () => import('./configuracao-agenda/configuracao-agenda.module').then(m => m.CinematicaConfiguracaoAgendaModule)
      },
      {
        path: 'agenda',
        loadChildren: () => import('./agenda/agenda.module').then(m => m.CinematicaAgendaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CinematicaEntityModule {}
