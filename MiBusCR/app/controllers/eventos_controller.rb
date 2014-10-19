class EventosController < ApplicationController
  before_action :set_ruta, only: [:show]
  #before_filter :restrict_access

  # GET /eventos
  # GET /eventos.json
  def index
    @eventos = Evento.all

    render json: @eventos.as_json(only: [:nombre, :descripcion, :tipo, :dia_y_hora, :lugar, :latitud, :longitud])
  end

  # GET /eventos/1
  # GET /eventos/1.json
  def show

    render json: @evento.as_json(only: [:nombre, :descripcion, :tipo, :dia_y_hora, :lugar, :latitud, :longitud])
  end

  # POST /eventos
  # POST /eventos.json
  def create
    head :unauthorized
  end

  # PATCH/PUT /eventos/1
  # PATCH/PUT /eventos/1.json
  def update
    head :unauthorized
  end

  # DELETE /eventos/1
  # DELETE /eventos/1.json
  def destroy
    head :unauthorized
  end

  private 
    def set_ruta
      begin  
        @evento = Evento.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
