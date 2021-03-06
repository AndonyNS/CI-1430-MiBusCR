class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :email
      t.string :password
      t.string :nombre
      t.date :fechaNac
      t.string :ciudad
      t.string :salt
      t.string :token
      t.boolean  :admin

      t.timestamps
    end
  end
end
