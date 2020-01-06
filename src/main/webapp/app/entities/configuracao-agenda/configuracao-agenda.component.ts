import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';
import { ConfiguracaoAgendaService } from './configuracao-agenda.service';
import { ConfiguracaoAgendaDeleteDialogComponent } from './configuracao-agenda-delete-dialog.component';

@Component({
  selector: 'jhi-configuracao-agenda',
  templateUrl: './configuracao-agenda.component.html'
})
export class ConfiguracaoAgendaComponent implements OnInit, OnDestroy {
  configuracaoAgenda?: IConfiguracaoAgenda[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected configuracaoAgendaService: ConfiguracaoAgendaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.configuracaoAgendaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IConfiguracaoAgenda[]>) => (this.configuracaoAgenda = res.body ? res.body : []));
      return;
    }
    this.configuracaoAgendaService.query().subscribe((res: HttpResponse<IConfiguracaoAgenda[]>) => {
      this.configuracaoAgenda = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInConfiguracaoAgenda();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IConfiguracaoAgenda): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInConfiguracaoAgenda(): void {
    this.eventSubscriber = this.eventManager.subscribe('configuracaoAgendaListModification', () => this.loadAll());
  }

  delete(configuracaoAgenda: IConfiguracaoAgenda): void {
    const modalRef = this.modalService.open(ConfiguracaoAgendaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.configuracaoAgenda = configuracaoAgenda;
  }
}
