import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgenda } from 'app/shared/model/agenda.model';
import { AgendaService } from './agenda.service';
import { AgendaDeleteDialogComponent } from './agenda-delete-dialog.component';

@Component({
  selector: 'jhi-agenda',
  templateUrl: './agenda.component.html'
})
export class AgendaComponent implements OnInit, OnDestroy {
  agenda?: IAgenda[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected agendaService: AgendaService,
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
      this.agendaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IAgenda[]>) => (this.agenda = res.body ? res.body : []));
      return;
    }
    this.agendaService.query().subscribe((res: HttpResponse<IAgenda[]>) => {
      this.agenda = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAgenda();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAgenda): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAgenda(): void {
    this.eventSubscriber = this.eventManager.subscribe('agendaListModification', () => this.loadAll());
  }

  delete(agenda: IAgenda): void {
    const modalRef = this.modalService.open(AgendaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.agenda = agenda;
  }
}
