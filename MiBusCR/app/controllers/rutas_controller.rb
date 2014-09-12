class RutasController < ApplicationController
  # GET /rutas
  # GET /rutas.json
  def index
    @rutas = Ruta.all

    render json: @rutas.as_json(only: [:id, :nombre, :frecuencia, :precio, :horario])
  end

  # GET /rutas/1
  # GET /rutas/1.json
  def show
    @ruta = Ruta.find(params[:id])

    render json: @ruta.as_json(only: [:id, :nombre, :frecuencia, :precio, :horario],include: [parada:{only: [:id]}])
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
end
