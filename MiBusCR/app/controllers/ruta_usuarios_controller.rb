class RutaUsuariosController < ApplicationController
  # GET /ruta_usuarios
  # GET /ruta_usuarios.json
  def index
    @ruta_usuarios = RutaUsuario.all

    render json: @ruta_usuarios
  end

  # GET /ruta_usuarios/1
  # GET /ruta_usuarios/1.json
  def show
    @ruta_usuario = RutaUsuario.find(params[:id])

    render json: @ruta_usuario
  end

  # POST /ruta_usuarios
  # POST /ruta_usuarios.json
  def create
    @ruta_usuario = RutaUsuario.new(params[:ruta_usuario])

    if @ruta_usuario.save
      render json: @ruta_usuario, status: :created, location: @ruta_usuario
    else
      render json: @ruta_usuario.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /ruta_usuarios/1
  # PATCH/PUT /ruta_usuarios/1.json
  def update
    @ruta_usuario = RutaUsuario.find(params[:id])

    if @ruta_usuario.update(params[:ruta_usuario])
      head :no_content
    else
      render json: @ruta_usuario.errors, status: :unprocessable_entity
    end
  end

  # DELETE /ruta_usuarios/1
  # DELETE /ruta_usuarios/1.json
  def destroy
    @ruta_usuario = RutaUsuario.find(params[:id])
    @ruta_usuario.destroy

    head :no_content
  end
end
