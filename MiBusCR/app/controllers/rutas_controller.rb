class RutasController < ApplicationController
  before_action :set_ruta, only: [:show]
  before_filter :restrict_access

  # GET /rutas
  # GET /rutas.json
  def index
    @rutas = Ruta.all

    render json: @rutas.as_json(only: [:id, :nombre, :frecuencia, :precio, :horario])
  end

  # GET /rutas/1
  # GET /rutas/1.json
  def show
    render json: @ruta.as_json(only: [:id, :nombre, :frecuencia, :precio, :horario],include: {ruta_parada:{only: [:parada_id, :tipo]},bus:{only:[:id]}})
  end

  # POST /rutas
  # POST /rutas.json
  def create
    head :unauthorized
  end

  # PATCH/PUT /rutas/1
  # PATCH/PUT /rutas/1.json
  def update
    head :unauthorized
  end

  # DELETE /rutas/1
  # DELETE /rutas/1.json
  def destroy
    head :unauthorized
  end

  private 
    # Never trust parameters from the scary internet, only allow the white list through. 
    def ruta_params 
          params.permit(:nombre, :frecuencia, :precio, :horario)
    end

    def set_ruta
      begin  
        @ruta = Ruta.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
